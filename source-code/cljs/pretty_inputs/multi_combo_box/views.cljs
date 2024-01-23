
(ns pretty-inputs.multi-combo-box.views
    (:require [fruits.random.api                        :as random]
              [fruits.vector.api                        :as vector]
              [pretty-engine.api                        :as pretty-engine]
              [pretty-inputs.chip-group.views           :as chip-group.views]
              [pretty-inputs.combo-box.views            :as combo-box.views]
              [pretty-inputs.multi-combo-box.attributes :as multi-combo-box.attributes]
              [pretty-inputs.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-chip-group
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:value-path (Re-Frame path vector)}
  [box-id {:keys [value-path] :as box-props}]
  (if-let [chips @(r/subscribe [:get-item value-path])]
          (if (vector/not-empty? chips)
              (let [group-id    (pretty-engine/input-id->subitem-id               box-id :chip-group)
                    group-props (multi-combo-box.prototypes/group-props-prototype box-id box-props)]
                   [chip-group.views/input group-id group-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-field
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [field-id    (pretty-engine/input-id->subitem-id               box-id :text-field)
        field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
       [combo-box.views/combo-box field-id field-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div (multi-combo-box.attributes/box-attributes box-id box-props)
        [:div (multi-combo-box.attributes/box-body-attributes box-id box-props)
              ;[pretty-engine/input-label                         box-id box-props]
              [multi-combo-box-chip-group                     box-id box-props]
              [multi-combo-box-field                          box-id box-props]]])

(defn input
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) input.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ; {:chip-group (map)(opt)
  ;   {:deletable? (boolean)(opt)
  ;     Default: true}
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :initial-options (vector)(opt)
  ;  :max-selection (integer)(opt)
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :option-component (Reagent component symbol)(opt)
  ;   Default: pretty-inputs.combo-box.views/default-option-component
  ;  :options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [multi-combo-box {...}]
  ;
  ; @usage
  ; [multi-combo-box :my-multi-combo-box {...}]
  ([box-props]
   [input (random/generate-keyword) box-props])

  ([box-id box-props]
   ; @note (tutorials#parametering)
   (fn [_ box-props]
       (let [box-props (multi-combo-box.prototypes/box-props-prototype box-id box-props)
             box-props (assoc-in box-props [:surface :content] [combo-box.views/combo-box-surface-content box-id box-props])]
            [multi-combo-box box-id box-props]))))
