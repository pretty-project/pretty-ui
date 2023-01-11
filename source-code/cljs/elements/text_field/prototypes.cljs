
(ns elements.text-field.prototypes
    (:require [candy.api                   :refer [param return]]
              [elements.input.helpers      :as input.helpers]
              [elements.text-field.helpers :as text-field.helpers]
              [random.api                  :as random]
              [vector.api                  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :icon-family (keyword)
  ;  :tab-indexed? (boolean)}
  [{:keys [icon] :as adornment-props}]
  (merge (if icon {:icon-family :material-icons-filled})
         {:color :default
          :tab-indexed? true}
         (param adornment-props)))

(defn end-adornments-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  (if emptiable? (let [empty-field-adornment-props (text-field.helpers/empty-field-adornment-props field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment-props))
                 (return end-adornments)))

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:autofill-name (keyword)
  ;  :border-color (keyword)
  ;  :border-radius (keyword)
  ;  :field-content-f (function)
  ;  :field-value-f (function)
  ;  :line-height (keyword)
  ;  :marker-position (keyword)
  ;  :type (keyword)
  ;  :value-path (vector)}
  [field-id {:keys [marker-color] :as field-props}]
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
          :line-height     :block
          :type            :text
          :value-path      (input.helpers/default-value-path field-id)}
         (if marker-color  {:marker-position :tr})
         (param field-props)))
