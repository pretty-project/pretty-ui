
(ns pretty-website.sidebar.side-effects
    (:require [pretty-website.sidebar.state :as sidebar.state]
              [re-frame.extra.api :as r]
              [scroll-lock.api              :as scroll-lock]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-sidebar!
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  [sidebar-id]
  (scroll-lock/add-scroll-prohibition! sidebar-id)
  (reset! sidebar.state/VISIBLE-SIDEBAR sidebar-id))

(defn hide-sidebar!
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  [sidebar-id]
  (scroll-lock/remove-scroll-prohibition! sidebar-id)
  (reset! sidebar.state/VISIBLE-SIDEBAR nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) sidebar-id
;
; @usage
; [:pretty-website.sidebar/show-sidebar! :my-sidebar]
(r/reg-fx :pretty-website.sidebar/show-sidebar! show-sidebar!)

; @param (keyword) sidebar-id
;
; @usage
; [:pretty-website.sidebar/hide-sidebar! :my-sidebar]
(r/reg-fx :pretty-website.sidebar/hide-sidebar! hide-sidebar!)
