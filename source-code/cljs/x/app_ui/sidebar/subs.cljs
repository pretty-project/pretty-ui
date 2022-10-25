
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sidebar.subs
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-sidebar-content
  ; @usage
  ;  (r get-sidebar-content db)
  ;
  ; @return (metamorphic-content)
  [db _]
  (get-in db [:ui :sidebar/content]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-hidden?
  ; @usage
  ;  (r sidebar-hidden? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db [:ui :sidebar/meta-items :hidden?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui.sidebar/get-sidebar-content]
(r/reg-sub :ui.sidebar/get-sidebar-content get-sidebar-content)

; @usage
;  [:ui.sidebar/sidebar-hidden?]
(r/reg-sub :ui.sidebar/sidebar-hidden? sidebar-hidden?)
