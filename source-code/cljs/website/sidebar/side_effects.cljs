
(ns website.sidebar.side-effects
    (:require [re-frame.api          :as r]
              [website.sidebar.state :as sidebar.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-sidebar!
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  [sidebar-id]
  (reset! sidebar.state/VISIBLE-SIDEBAR sidebar-id))

(defn hide-sidebar!
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  [_]
  (reset! sidebar.state/VISIBLE-SIDEBAR nil))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-fx :website.sidebar/show-sidebar! show-sidebar!)

; @ignore
(r/reg-fx :website.sidebar/hide-sidebar! hide-sidebar!)
