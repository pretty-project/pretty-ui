
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.magic-widget.effects
    (:require [developer-tools.magic-widget.views :as magic-widget.views]
              [re-frame.api                       :as r :refer [r]]
              [x.app-gestures.api                 :as x.gestures]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer-tools.magic-widget/render-widget!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r x.gestures/init-view-handler! db :developer-tools.magic-widget/handler
                                                     {:default-view-id :re-frame-browser :reinit? false})
       :dispatch [:ui/render-popup! :developer-tools.magic-widget/view
                                    {:content #'magic-widget.views/view}]}))
