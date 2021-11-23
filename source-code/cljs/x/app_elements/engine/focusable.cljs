
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
    (:require [x.app-core.api                   :as a]
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
  #(a/dispatch [:x.app-environment.element-handler/blur!]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/focus-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (let [target-id (targetable/element-id->target-id element-id)]
           [:x.app-environment.element-handler/focus! target-id])))

(a/reg-event-fx
  :x.app-elements/blur-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ element-id]]
      (let [target-id (targetable/element-id->target-id element-id)]
           [:x.app-environment.element-handler/blur! target-id])))
