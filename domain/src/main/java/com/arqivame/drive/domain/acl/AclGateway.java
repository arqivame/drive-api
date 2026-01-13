package com.arqivame.drive.domain.acl;

import java.util.Optional;

public interface AclGateway {

    Acl create(Acl acl);

    Optional<Acl> findByResource(Resource<?> resource);

}
