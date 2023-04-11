
(ns elements.chip-group.views
    (:require [elements.chip-group.attributes :as chip-group.attributes]
              [elements.chip-group.prototypes :as chip-group.prototypes]
              [elements.chip.views            :as chip.views]
              [elements.element.views         :as element.views]
              [metamorphic-content.api        :as metamorphic-content]
              [random.api                     :as random]
              [re-frame.api                   :as r]
              [vector.api                     :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-chips
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:placeholder (metamorphic-content)(opt)
  ;  :value-path (vector)}
  [group-id {:keys [placeholder value-path] :as group-props}]
  (let [chips @(r/subscribe [:get-item value-path])]
       (if (vector/nonempty? chips)

           ; Iterating over the data read from the value-path if it's a nonempty vector
           ; Every item of the vector displayed on a chip with applying the 'chip-label-f' on the item
           (letfn [(f [chip-list chip-dex chip]
                      (let [chip-props (chip-group.prototypes/chip-props-prototype group-id group-props chip-dex chip)]
                           (conj chip-list [chip.views/element chip-props])))]
                  (reduce-kv f [:div {:class :e-chip-group--chips}] chips))

           ; Displaying the placeholder if the data from the value-path is NOT a nonempty vector
           (if placeholder [:div {:class :e-chip-group--chips-placeholder :data-font-size :s :data-line-height :text-block}
                                 (metamorphic-content/resolve placeholder)]))))

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (chip-group.attributes/chip-group-attributes group-id group-props)
        [element.views/element-label group-id group-props]
        [:div (chip-group.attributes/chip-group-body-attributes group-id group-props)
              [chip-group-chips group-id group-props]]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :chip-label-f (function)(opt)
  ;   Default: return
  ;  :deletable? (boolean)(opt)
  ;   Default: false
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :placeholder (metamorphic-content)(opt)
  ;  :style (map)(opt)
  ;  :value-path (vector)(opt)}
  ;
  ; @usage
  ; [chip-group {...}]
  ;
  ; @usage
  ; [chip-group :my-chip-group {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (let [group-props (chip-group.prototypes/group-props-prototype group-id group-props)]
        [chip-group group-id group-props])))
