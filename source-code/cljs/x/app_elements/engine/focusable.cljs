
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.01
; Description:
; Version: v0.3.6
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.focusable
    (:require [x.app-core.api :as a]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blur-element-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (function)
  [_]
  ; Ha a kattintható elemeken a kattintás után is ott marad a fókusz, akkor
  ; az ENTER és SPACE billentyűk lenyomására újból megtörténik a kattintási
  ; esemény. Ennek elkerülése érdekében szükséges megszűntetni a fókuszt
  ; az elemen!
  #(a/dispatch [:environment/blur-element!]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/focus-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (let [target-id (targetable/element-id->target-id element-id)]
           {:environment/focus-element! target-id})))

(a/reg-event-fx
  :elements/blur-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (let [target-id (targetable/element-id->target-id element-id)]
           {:environment/blur-element! target-id})))
