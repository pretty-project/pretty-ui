
(ns pretty-website.sidebar.effects
    (:require [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-website.sidebar/show-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ; [:pretty-website.sidebar/show-sidebar! :my-sidebar]
  (fn [_ [_ sidebar-id]]
      {:fx [:pretty-website.sidebar/show-sidebar! sidebar-id]}))

(r/reg-event-fx :pretty-website.sidebar/hide-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ; [:pretty-website.sidebar/hide-sidebar! :my-sidebar]
  (fn [_ [_ sidebar-id]]
      {:fx [:pretty-website.sidebar/hide-sidebar! sidebar-id]}))
