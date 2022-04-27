
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.app-menu.effects
    (:require [x.app-core.api             :as a :refer [r]]
              [x.app-gestures.api         :as gestures]
              [x.app-ui.api               :as ui]
              [x.app-views.app-menu.views :as app-menu.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-app-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db :views.app-menu/handler {:default-view-id :main})
       :dispatch [:ui/render-popup! :views.app-menu/view
                                    {:body   #'app-menu.views/body
                                     :header #'ui/close-popup-header
                                     :horizontal-align :left
                                     :min-width        :xs}]}))
