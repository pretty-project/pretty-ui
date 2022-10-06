
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sidebar.effects
    (:require [re-frame.api            :as r :refer [r]]
              [x.app-ui.sidebar.events :as sidebar.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui.sidebar/render!
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @usage
  ;  [:ui.sidebar/render! :my-content]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ sidebar-id sidebar-props]]
      {:db (r sidebar.events/render-sidebar! db sidebar-id sidebar-props)}))
