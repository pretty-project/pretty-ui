
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
  ;  (r render-sidebar! db :my-content {...})
  ;
  ; @return (map)
  [db [_ sidebar-id sidebar-props]]
  (assoc-in db [:ui :sidebar/content] sidebar-props))

(defn remove-sidebar!
  ; @param (keyword) sidebar-id
  ;
  ; @usage
  ;  (r remove-sidebar! db :my-content)
  ;
  ; @return (map)
  [db [_ sidebar-id]]
  (-> db (dissoc-in [:ui :sidebar/content])
         (dissoc-in [:ui :sidebar/meta-items])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-sidebar!
  ; @usage
  ;  (r hide-sidebar! db)
  ;
  ; @return (map)
  [db _]
  (assoc-in db [:ui :sidebar/meta-items :sidebar-hidden?] true))

(defn show-sidebar!
  ; @usage
  ;  (r show-sidebar! db)
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:ui :sidebar/meta-items :sidebar-hidden?]))
