
(ns elements.chip-group.views
    (:require [elements.chip-group.attributes :as chip-group.attributes]
              [elements.chip-group.prototypes :as chip-group.prototypes]
              [elements.chip.views            :as chip.views]
              [elements.element.views         :as element.views]
              [metamorphic-content.api        :as metamorphic-content]
              [pretty-presets.api             :as pretty-presets]
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
  ; {:chips (maps in vector)(opt)
  ;  :chips-path (Re-Frame path vector)(opt)
  ;  :placeholder (metamorphic-content)(opt)}
  [group-id {:keys [chips chips-path placeholder] :as group-props}]
  ; XXX#2781 (source-code/cljs/elements/input/env.cljs)
  ; Just like the optionable inputs the chip-group has multiple chip sources as well:
  ; A) The :chips property
  ; B) The application state (:chips-path property)
  (let [chips (or chips @(r/subscribe [:get-item chips-path]))]
       (if (vector/nonempty? chips)

           ; Iterating over the chips if it's a nonempty vector
           ; Every item in the vector displayed on a chip with applying the 'chip-label-f' on the item
           (letfn [(f [chip-list chip-dex chip]
                      (let [chip-props (chip-group.prototypes/chip-props-prototype group-id group-props chip-dex chip)]
                           (conj chip-list [chip.views/element chip-props])))]
                  (reduce-kv f [:div {:class :e-chip-group--chips}] chips))

           ; Displaying the placeholder if the chips is NOT a nonempty vector
           (if placeholder [:div (chip-group.attributes/chip-group-placeholder-attributes group-id group-props)
                                 (metamorphic-content/compose placeholder)]))))

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
  ; @warning
  ; The {:deletable? true} setting only works when the chip values are not statically provided
  ; with the {:chips [...]} property but dinamically provided by using the {:chips-path [...]} property!
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :chip-label-f (function)(opt)
  ;   Default: return
  ;  :chips (maps in vector)(opt)
  ;  :chips-path (Re-Frame path vector)(opt)
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
  ;   Same as the :indent property.
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [chip-group {...}]
  ;
  ; @usage
  ; [chip-group :my-chip-group {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   (fn [_ group-props] ; XXX#0106 (README.md#parametering)
       (let [group-props (pretty-presets/apply-preset                          group-props)
             group-props (chip-group.prototypes/group-props-prototype group-id group-props)]
            [chip-group group-id group-props]))))
