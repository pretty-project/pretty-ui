
(ns elements.multi-combo-box.views
    (:require [elements.chip-group.views           :as chip-group.views]
              [elements.combo-box.views            :as combo-box.views]
              [elements.element.views              :as element.views]
              [elements.multi-combo-box.attributes :as multi-combo-box.attributes]
              [elements.multi-combo-box.helpers    :as multi-combo-box.helpers]
              [elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]
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
  ; {:value-path (vector)}
  [box-id {:keys [value-path] :as box-props}]
  (if-let [chips @(r/subscribe [:get-item value-path])]
          (if (vector/nonempty? chips)
              (let [group-id    (multi-combo-box.helpers/box-id->group-id         box-id)
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
  (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
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
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; The multi-combo-box element is based on the text-field element.
  ; For more information check out the documentation of the text-field element.
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
  ;  :on-select (metamorphic-event)(opt)
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :option-component (component)(opt)
  ;   Default: elements.combo-box/default-option-component
  ;  :options (vector)(opt)
  ;  :options-path (vector)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ; [multi-combo-box {...}]
  ;
  ; @usage
  ; [multi-combo-box :my-multi-combo-box {...}]
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (let [box-props (multi-combo-box.prototypes/box-props-prototype box-id box-props)
         box-props (assoc box-props :surface [combo-box.views/combo-box-surface box-id box-props])]
        [multi-combo-box box-id box-props])))
