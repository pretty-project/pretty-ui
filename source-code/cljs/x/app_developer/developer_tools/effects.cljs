
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.effects
    (:require [re-frame.api                          :as r :refer [r]]
              [x.app-developer.developer-tools.views :as developer-tools.views]
              [x.app-gestures.api                    :as gestures]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer/test!
  ; @usage
  ;  [:developer/test!]
  [:ui/render-bubble! {:body "It works!"}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer/render-developer-tools!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db :developer.developer-tools/handler
                                                   {:default-view-id :re-frame-browser :reinit? false})
       :dispatch [:ui/render-popup! :developer.developer-tools/view
                                    {:content #'developer-tools.views/view}]}))
