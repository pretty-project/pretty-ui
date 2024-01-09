
(ns pretty-inputs.chip-group.views
    (:require [fruits.random.api                     :as random]
              [fruits.vector.api                     :as vector]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-inputs.chip-group.attributes :as chip-group.attributes]
              [pretty-inputs.chip-group.prototypes :as chip-group.prototypes]
              [pretty-elements.api            :as pretty-elements]
              [pretty-inputs.core.views         :as core.views]
              [pretty-presets.api                    :as pretty-presets]
              [re-frame.api                          :as r]))

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
  (let [chips (or chips @(r/subscribe [:get-item chips-path]))]
       (if (vector/nonempty? chips)

           ; Iterates over the 'chips' vector if it's nonempty.
           ; Every item in the vector displayed on a chip with the 'chip-label-f' function applied on the item.
           (letfn [(f0 [chip-list chip-dex chip]
                       (let [chip-props (chip-group.prototypes/chip-props-prototype group-id group-props chip-dex chip)]
                            (conj chip-list [pretty-elements/chip chip-props])))]
                  (reduce-kv f0 [:div {:class :pi-chip-group--chips}] chips))

           ; Displays the placeholder if the 'chips' value is NOT a nonempty vector.
           (if placeholder [:div (chip-group.attributes/chip-group-placeholder-attributes group-id group-props)
                                 (metamorphic-content/compose placeholder)]))))

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (chip-group.attributes/chip-group-attributes group-id group-props)
        [core.views/input-label                      group-id group-props]
        [:div (chip-group.attributes/chip-group-body-attributes group-id group-props)
              [chip-group-chips group-id group-props]]])

(defn input
  ; @important
  ; The {:deletable? true} setting only works when the chip values are not provided as static data
  ; via the ':chips' property, but provided dinamically by using the ':chips-path' property!
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
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
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
   [input (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parametering)
   (fn [_ group-props]
       (let [group-props (pretty-presets/apply-preset                          group-props)
             group-props (chip-group.prototypes/group-props-prototype group-id group-props)]
            [chip-group group-id group-props]))))
