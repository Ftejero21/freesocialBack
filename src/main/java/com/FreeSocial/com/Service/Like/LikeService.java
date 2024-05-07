package com.FreeSocial.com.Service.Like;

public interface LikeService {

    public void darLikeALaPublicacion(Long publicacionId);

    public boolean usuarioHaDadoLike(Long publicacionId);
}
