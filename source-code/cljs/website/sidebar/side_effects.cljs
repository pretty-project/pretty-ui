
(ns website.sidebar.side-effects
    (:require [re-frame.api          :as r]
              [scroll-lock.api       :as scroll-lock]
              [website.sidebar.state :as sidebar.state]))

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

; @ignore
(r/reg-fx :website.sidebar/show-sidebar! show-sidebar!)

; @ignore
(r/reg-fx :website.sidebar/hide-sidebar! hide-sidebar!)