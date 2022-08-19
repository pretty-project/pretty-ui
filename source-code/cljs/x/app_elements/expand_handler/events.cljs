
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.expand-handler.events
    (:require [x.app-core.api                     :as a :refer [r]]
              [x.app-elements.engine.element      :as element]
              [x.app-elements.expand-handler.subs :as expand-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expand-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r element/set-element-prop! db element-id :expanded? true))

(defn compress-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r element/set-element-prop! db element-id :expanded? false))

(defn toggle-element-expansion!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (if (r expand-handler.subs/element-expanded? db element-id)
      (r compress-element!                     db element-id)
      (r expand-element!                       db element-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/expand-element! expand-element!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/compress-element! compress-element!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements/toggle-element-expansion! toggle-element-expansion!)
