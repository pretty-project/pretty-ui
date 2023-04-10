
(ns elements.text-field.prototypes
    (:require [elements.input.utils    :as input.utils]
              [elements.text-field.env :as text-field.env]
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
         {:color :default
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
  ; Az empty-field-adornment-props térkép értéke megváltozik, ha a szövegmező
  ; értéke üresről nem üres stringre vált, mert a az empty-field-adornment gomb
  ; {:disabled? true} állapotban van, amíg a szövegmező értéke üres.
  ;
  ; Az a komponens és leszármazottai, amelyikben az end-adornments-prototype függvény
  ; meghívásra kerül, minden esetben újrarenderelődnek, amikor a szövegmező értéke
  ; üres és nem üres állapot között változik.
  (if emptiable? (let [empty-field-adornment-props (text-field.env/empty-field-adornment-props field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment-props))
                 (return end-adornments)))

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:border-color (keyword)(opt)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:autofill-name (keyword)
  ;  :border-position (keyword)
  ;  :border-width (keyword)
  ;  :field-content-f (function)
  ;  :field-value-f (function)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :line-height (keyword)
  ;  :marker-position (keyword)
  ;  :type (keyword)
  ;  :value-path (vector)}
  [field-id {:keys [border-color marker-color] :as field-props}]
  ; BUG#6782
  ; https://stackoverflow.com/questions/12374442/chrome-ignores-autocomplete-off
  ;
  ; The Chrome browser ...
  ; ... ignores the {:autocomplete "off"} setting,
  ; ... ignores the {:autocomplete "new-*"} setting,
  ; ... acknowledges the {:name ...} value.
  ;
  ; By using a generated :autofill-name value, the browser cannot suggest values
  ; to the field.
  ; If you want the browser to suggest values for a field, pass an understandable
  ; value! (E.g. :phone-number)
  (merge {:autofill-name   (random/generate-keyword)
          :field-content-f return
          :field-value-f   return
          :font-size       :s
          :font-weight     :normal
          :line-height     :text-block
          :type            :text
          :value-path      (input.utils/default-value-path field-id)}
         (if border-color  {:border-position :all
                            :border-width    :xxs})
         (if marker-color  {:marker-position :tr})
         (param field-props)))
