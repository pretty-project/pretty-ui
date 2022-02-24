
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.app-menu.effects
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-gestures.api :as gestures]
              [x.app-ui.api       :as ui]
              [x.app-views.app-menu.views :as app-menu.views]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views.app-menu/render-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r gestures/init-view-handler! db :app-views.app-menu/handler {:default-view-id :main})
       :dispatch [:ui/add-popup! :app-views.app-menu/view
                                 {:body   #'app-menu.views/body
                                  :header #'ui/close-popup-header
                                  :horizontal-align :left
                                  :min-width        :xs}]}))
