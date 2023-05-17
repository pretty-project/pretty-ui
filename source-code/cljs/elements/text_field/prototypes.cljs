
(ns elements.text-field.prototypes
    (:require [elements.input.utils    :as input.utils]
              [elements.text-field.env :as text-field.env]
              [loop.api                :refer [<-walk]]
              [noop.api                :refer [param return]]
              [random.api              :as random]
              [vector.api              :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :color (keyword)
  ;  :icon-family (keyword)
  ;  :tab-indexed? (boolean)}
  [field-props {:keys [icon label on-click] :as adornment-props}]
  (merge (if icon     {:icon-family    :material-symbols-outlined
                       :icon-size      :s})
         (if label    {:font-size      :xxs
                       :letter-spacing :auto
                       :line-height    :text-block})
         (if on-click {:click-effect :opacity})
         {:color        :default
          :tab-indexed? true}
         (param adornment-props)
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
  ; Therefore every component that calls this 'end-adornments-prototype' function
  ; rerenders when the field value changes between empty and nonempty strings.
  (if emptiable? (let [empty-field-adornment-props (text-field.env/empty-field-adornment-props field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment-props))
                 (return end-adornments)))

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:border-color (keyword)(opt)
  ;  :marker-color (keyword)(opt)
  ;  :on-blur (Re-Frame metamorphic-event)(opt)
  ;  :on-focus (Re-Frame metamorphic-event)(opt)
  ;  :on-type-ended (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:autofill-name (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :field-content-f (function)
  ;  :field-value-f (function)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :form-id (keyword)
  ;  :line-height (keyword)
  ;  :marker-position (keyword)
  ;  :type (keyword)
  ;  :value-path (Re-Frame path vector)
  ;  :width (keyword)}
  [field-id {:keys [border-color marker-color on-blur on-focus on-type-ended] :as field-props}]
  ; BUG#6782
  ; https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
  ;
  ; The Chrome browser ...
  ; ... ignores the {:autocomplete "off"} setting,
  ; ... ignores the {:autocomplete "new-*"} setting,
  ; ... acknowledges the {:name ...} value.
  ;
  ; By using random generated :autofill-name value, the browser cannot suggest
  ; values to the field.
  ; If you want the browser to suggest values, pass an understandable value!
  ; (E.g. :phone-number)
  ;
  ; XXX#5068
  ; By using the '<-walk' function the :on-blur, :on-type-ended and :on-focus
  ; events take the 'field-props' map AFTER it being merged with the default values!
  (<-walk {:autofill-name   (random/generate-keyword)
           :field-content-f return
           :field-value-f   return
           :font-size       :s
           :form-id         field-id
           :font-weight     :normal
           :line-height     :text-block
           :type            :text
           :value-path      (input.utils/default-value-path field-id)
           :width           :content}
         (fn [%] (merge % (if border-color {:border-position :all
                                            :border-width    :xxs})
                          (if marker-color {:marker-position :tr})))
         (fn [%] (merge % field-props))
         (fn [%] (merge % {:on-blur       {:dispatch-n [on-blur       [:elements.text-field/field-blurred field-id %]]}
                           :on-focus      {:dispatch-n [on-focus      [:elements.text-field/field-focused field-id %]]}
                           :on-type-ended {:dispatch-n [on-type-ended [:elements.text-field/type-ended    field-id %]]}}))))
