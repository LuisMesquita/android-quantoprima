package br.com.mobiclub.quantoprima.util;

import br.com.mobiclub.quantoprima.core.service.api.entity.Notification;

/**
 * Created by wildsonsantos on 16/09/15.
 */
public interface NotificacaoCallBack {

    void removerNotificacao(Notification notification);
    void openNotificacao(Notification notification);
}
