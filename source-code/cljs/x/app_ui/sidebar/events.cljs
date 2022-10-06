
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sidebar.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [re-frame.api   :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-sidebar!
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ;
  ; @usage
  ;  (r ui/render-sidebar! db :my-content)
  ;
  ; @return (metamorphic-content)
  [db [_ sidebar-id sidebar-props]]
  (assoc-in db [:ui :sidebar/content] sidebar-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-sidebar!
  ; @usage
  ;  (r ui/hide-sidebar! db)
  ;
  ; @return (boolean)
  [db _]
  (assoc-in db [:ui :sidebar/meta-items :sidebar-hidden?] true))

(defn show-sidebar!
  ; @usage
  ;  (r ui/show-sidebar! db)
  ;
  ; @return (boolean)
  [db _]
  (dissoc-in db [:ui :sidebar/meta-items :sidebar-hidden?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui.sidebar/hide-sidebar!]
(r/reg-event-db :ui.sidebar/hide-sidebar! hide-sidebar!)

; @usage
;  [:ui.sidebar/show-sidebar!]
(r/reg-event-db :ui.sidebar/show-sidebar! show-sidebar!)
