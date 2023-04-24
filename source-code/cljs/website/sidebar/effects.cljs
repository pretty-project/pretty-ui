
(ns website.sidebar.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website.sidebar/show-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ; [:website.sidebar/show-sidebar! :my-sidebar]
  (fn [_ [_ sidebar-id]]
      {:fx [:website.sidebar/show-sidebar! sidebar-id]}))

(r/reg-event-fx :website.sidebar/hide-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ; [:website.sidebar/hide-sidebar! :my-sidebar]
  (fn [_ [_ sidebar-id]]
      {:fx [:website.sidebar/hide-sidebar! sidebar-id]}))