
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.field
    (:require [dom.api                               :as dom]
              [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.css                        :as css]
              [mid-fruits.string                     :as string]
              [mid-fruits.vector                     :as vector]
              [x.app-core.api                        :as a :refer [r]]
              [x.app-elements.surface-handler.events :as surface-handler.events]
              [x.app-elements.engine.element         :as element]
              [x.app-elements.input.events           :as input.events]
              [x.app-elements.input.helpers          :as input.helpers]
              [x.app-elements.input.subs           :as input.subs]
              [x.app-elements.target-handler.helpers :as target-handler.helpers]
              [x.app-environment.api                 :as environment]

              [x.app-db.api :as db]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def FIELD-LINE-HEIGHT 24)

; @constant (px)
(def FIELD-VERTICAL-PADDING 3)

; @constant (px)
(def FIELD-BORDER-WIDTH 1)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props->line-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;  {:multiline? (boolean)(opt)
  ;   :value (string)}
  ;
  ; @return (integer)
  [{:keys [multiline? value]}]
  (if multiline? ; If field is multiline ...
                 (let [line-count (-> value string/count-newlines inc)]
                      ; BUG#1481
                      ; A textarea element magassága minimum 2 sor magasságú kell legyen,
                      ; különben az egy sorba írt - a textarea szélességébe ki nem férő -
                      ; szöveg nem törik meg automatikusan
                      ; Google Chrome Version 89.0.4389.114
                      (inc line-count))
                 ; If field is NOT multiline ...
                 (return 1)))

(defn field-props->field-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
  (+ (* FIELD-LINE-HEIGHT (field-props->line-count field-props))
     (* FIELD-VERTICAL-PADDING 2)
     (* FIELD-BORDER-WIDTH     2)))

(defn field-props->field-style
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:height (string)}
  [field-props]
  {:height (-> field-props field-props->field-height css/px)})



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-field-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (string)
  [db [_ field-id]]
  (let [input-value (r input.subs/get-input-value db field-id)]
       (str input-value)))

(defn field-filled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (let [field-value (r get-field-value db field-id)]
       (string/nonempty? field-value)))

(defn field-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (let [field-filled? (r field-filled? db field-id)]
       (not field-filled?)))

(a/reg-sub :elements/field-empty? field-empty?)

(defn field-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (r input.subs/value-changed? db field-id))

(defn field-focused?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (r element/get-element-prop db field-id :focused?))

(defn get-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :field-empty? (boolean)
  ;   :invalid-message (metamorphic-content)
  ;   :value (string)}
  [db [_ field-id]])
  ;(merge {:field-empty?   (r field-empty?    db field-id)
  ;        :field-changed? (r field-changed?  db field-id)
  ;        :value          (r get-field-value db field-id)
  ;       ; 1.
  ;       (if (r input.subs/value-invalid-warning? db field-id)
  ;           {:border-color :warning :invalid-message (r input.subs/get-invalid-message db field-id)}
  ;       ; 2.
  ;       (if (r input.subs/required-warning? db field-id)
  ;           {:border-color :warning :invalid-message :please-fill-out-this-field}]])



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-field-as-blurred!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/set-element-prop! db field-id :focused? false))

(a/reg-event-db :elements/mark-field-as-blurred! mark-field-as-blurred!)

(defn mark-field-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (r element/set-element-prop! db field-id :focused? true))

(a/reg-event-db :elements/mark-field-as-focused! mark-field-as-focused!)

(defn empty-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (let [value-path (r element/get-element-prop db field-id :value-path)]
       (assoc-in db value-path string/empty-string)))

(defn set-field-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ field-id value]]
  (let [field-value (str value)]
       (r input.events/set-value! db field-id field-value)))
