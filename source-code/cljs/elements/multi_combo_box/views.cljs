
(ns elements.multi-combo-box.views
    (:require [elements.chip-group.views           :as chip-group.views]
              [elements.combo-box.views            :as combo-box.views]
              [elements.element.views              :as element.views]
              [elements.multi-combo-box.attributes :as multi-combo-box.attributes]
              [elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [elements.multi-combo-box.utils      :as multi-combo-box.utils]
              [random.api                          :as random]
              [re-frame.api                        :as r]
              [vector.api                          :as vector]))

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
          (if (vector/nonempty? chips)
              (let [group-id    (multi-combo-box.utils/box-id->group-id           box-id)
                    group-props (multi-combo-box.prototypes/group-props-prototype box-id box-props)]
                   [chip-group.views/element group-id group-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-field
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [field-id    (multi-combo-box.utils/box-id->field-id           box-id)
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
              [element.views/element-label                    box-id box-props]
              [multi-combo-box-chip-group                     box-id box-props]
              [multi-combo-box-field                          box-id box-props]]])

(defn element
  ; @info
  ; XXX#0711 (source-code/cljs/elements/text_field/views.cljs)
  ; The 'multi-combo-box' element is based on the 'text-field' element.
  ; For more information check out the documentation of the 'text-field' element.
  ;
  ; @description
  ; The 'multi-combo-box' element writes its actual value into the Re-Frame state
  ; delayed after the user stopped typing or without a delay when the user
  ; leaves the field!
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ; {:chip-group (map)(opt)
  ;   For more information check out the documentation of the chip-group element.
  ;   {:deletable? (boolean)(opt)
  ;     Default: true}
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :initial-options (vector)(opt)
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :option-component (Reagent component symbol)(opt)
  ;   Default: elements.combo-box/default-option-component
  ;  :options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [multi-combo-box {...}]
  ;
  ; @usage
  ; [multi-combo-box :my-multi-combo-box {...}]
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (fn [_ box-props] ; XXX#0106 (README.md#parametering)
       (let [box-props (multi-combo-box.prototypes/box-props-prototype box-id box-props)
             box-props (assoc-in box-props [:surface :content] [combo-box.views/combo-box-surface-content box-id box-props])]
            [multi-combo-box box-id box-props]))))
