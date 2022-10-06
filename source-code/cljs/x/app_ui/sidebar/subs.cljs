
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
  ;  (r ui/get-sidebar-content db)
  ;
  ; @return (metamorphic-content)
  [db _]
  (get-in db [:ui :sidebar/content]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-hidden?
  ; @usage
  ;  (r ui/sidebar-hidden? db)
  ;
  ; @return (boolean)
  [db _]
  (get-in db [:ui :sidebar/meta-items :hidden?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui.sidebar/get-content]
(r/reg-sub :ui.sidebar/get-content get-sidebar-content)

; @usage
;  [:ui.sidebar/hidden?]
(r/reg-sub :ui.sidebar/hidden? sidebar-hidden?)
