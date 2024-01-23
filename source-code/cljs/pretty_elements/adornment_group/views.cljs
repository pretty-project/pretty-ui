
(ns pretty-elements.adornment-group.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [fruits.vector.api                          :as vector]
              [pretty-elements.adornment-group.attributes :as adornment-group.attributes]
              [pretty-elements.adornment-group.prototypes :as adornment-group.prototypes]
              [pretty-elements.adornment.views :as adornment.views]
              [pretty-presets.api                         :as pretty-presets]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:adornments (maps in vector)(opt)}
  [group-id {:keys [adornments] :as group-props}]
  [:div (adornment-group.attributes/adornment-group-attributes group-id group-props)
        [:div (adornment-group.attributes/adornment-group-body-attributes group-id group-props)
              (if (vector/not-empty? adornments)
                  (letfn [(f0 [adornment-props] [adornment.views/element adornment-props])]
                         (hiccup/put-with [:<>] adornments f0)))]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:adornments (maps in vector)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [adornment-group {...}]
  ;
  ; @usage
  ; [adornment-group :my-adornment-group {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parametering)
   (fn [_ group-props]
       (let [group-props (pretty-presets/apply-preset group-props)]
             ; group-props (adornment-group.prototypes/group-props-prototype group-props)
            [adornment-group group-id group-props]))))
