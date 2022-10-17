
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

(r/reg-event-fx :ui.sidebar/render-sidebar!
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @usage
  ;  [:ui.sidebar/render-sidebar! :my-sidebar {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ sidebar-id sidebar-props]]
      {:db (r sidebar.events/render-sidebar! db sidebar-id sidebar-props)}))

(r/reg-event-fx :ui.sidebar/remove-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ;  [:ui.sidebar/remove-sidebar! :my-sidebar]
  (fn [{:keys [db]} [_ sidebar-id]]
      {:db (r sidebar.events/remove-sidebar! db sidebar-id)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui.sidebar/show-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ;  [:ui.sidebar/show-sidebar! :my-sidebar]
  (fn [{:keys [db]} [_ sidebar-id]]
      {:db (r sidebar.events/show-sidebar! db sidebar-id)}))

(r/reg-event-fx :ui.sidebar/hide-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ;  [:ui.sidebar/hide-sidebar! :my-sidebar]
  (fn [{:keys [db]} [_ sidebar-id]]
      {:db (r sidebar.events/hide-sidebar! db sidebar-id)}))
