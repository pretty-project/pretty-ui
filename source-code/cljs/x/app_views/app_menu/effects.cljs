

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.app-menu.effects
    (:require [x.app-core.api             :as a :refer [r]]
              [x.app-gestures.api         :as gestures]
              [x.app-views.app-menu.views :as app-menu.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-app-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db :views.app-menu/handler {:default-view-id :main})
       :dispatch [:ui/render-popup! :views.app-menu/view
                                    {:content #'app-menu.views/view}]}))
