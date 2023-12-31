
(ns pretty-elements.text-field.prototypes
    (:require [fruits.loop.api                :refer [<-walk]]
              [fruits.noop.api                :refer [return]]
              [fruits.vector.api              :as vector]
              [pretty-elements.input.utils    :as input.utils]
              [pretty-elements.text-field.env :as text-field.env]
              [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :icon-family (keyword)
  ;  :tab-indexed? (boolean)
  ;  :text-color (keyword or string)}
  [field-props {:keys [icon label on-click timeout] :as adornment-props}]
  (merge (if icon     {:icon-family    :material-symbols-outlined
                       :icon-size      :s})
         (if label    {:font-size      :xxs
                       :letter-spacing :auto
                       :line-height    :text-block})
         (if on-click {:click-effect   :opacity})
         {:tab-indexed? true
          :text-color   :default}
         (-> adornment-props)
         ; Inherits the reveal effect from the field-props into the adornment-props
         (select-keys field-props [:reveal-effect])))

(defn end-adornments-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [field-id {:keys [emptiable? end-adornments] :as field-props}]
  ; XXX#5100
  ; The 'empty-field-adornment-props' map value changes every time when the field
  ; content get change from an empty string to a nonempty string (or vica versa),
  ; because it contains the ':disabled?' value that depends on whether the field
  ; content is an empty string.
  ; Therefore, every component that calls this 'end-adornments-prototype' function
  ; rerenders when the field value changes between empty and nonempty strings.
  (if emptiable? (let [empty-field-adornment-props (text-field.env/empty-field-adornment-props field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment-props))
                 (-> end-adornments)))

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:border-color (keyword or string)(opt)
  ;  :marker-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :field-content-f (function)
  ;  :field-value-f (function)
  ;  :font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :form-id (keyword)
  ;  :line-height (keyword, px or string)
  ;  :marker-position (keyword)
  ;  :type (keyword)
  ;  :value-path (Re-Frame path vector)}
  [field-id {:keys [border-color marker-color] :as field-props}]
  ; XXX#5068
  ; By using the '<-walk' function the ':on-blur', ':on-type-ended' and ':on-focus'
  ; events take the 'field-props' map AFTER it gets merged with the default values!
  (<-walk {:field-content-f return
           :field-value-f   return
           :font-size       :s
           :form-id         field-id
           :font-weight     :normal
           :line-height     :text-block
           :type            :text
           :value-path      (input.utils/default-value-path field-id)}
          (fn [%] (merge % (if border-color {:border-position :all
                                             :border-width    :xxs})
                           (if marker-color {:marker-position :tr})))
          (fn [%] (merge % field-props))
          (fn [%] (merge % {:on-blur       [:pretty-elements.text-field/field-blurred field-id %]
                            :on-focus      [:pretty-elements.text-field/field-focused field-id %]
                            :on-type-ended [:pretty-elements.text-field/type-ended    field-id %]}))))
