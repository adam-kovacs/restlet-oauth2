import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Reference;
import org.restlet.ext.oauth.TokenVerifier;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

/**
 * Simple OAuth 2.0 protected application.
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class SampleApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/status", StatusServerResource.class);

        /*
         * Since Role#hashCode and Role#equals are not implemented,
         * RoleAuthorizer cannot be used.
         */
        // RoleAuthorizer roleAuthorizer = new RoleAuthorizer();
        // roleAuthorizer.setAuthorizedRoles(Scopes.toRoles("status"));
        // roleAuthorizer.setNext(router);

        ChallengeAuthenticator bearerAuthenticator = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_OAUTH_BEARER, "OAuth2Sample");
        bearerAuthenticator.setVerifier(new TokenVerifier(new Reference(
                "riap://component/oauth/token_auth")));
        bearerAuthenticator.setNext(router);

        return bearerAuthenticator;
    }
}