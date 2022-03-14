
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification.effects
    (:require [x.app-core.api                             :as a :refer [r]]
              [x.app-environment.api                      :as environment]
              [x.app-ui.api                               :as ui]
              [x.app-views.no-internet-notification.views :as no-internet-notification.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/blow-no-internet-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (if (and (r environment/browser-offline? db)
               (r ui/application-interface?    db))
          [:ui/blow-bubble! ::notification
                            {:body        #'no-internet-notification.views/body
                             :autopop?    false
                             :user-close? false}])))
