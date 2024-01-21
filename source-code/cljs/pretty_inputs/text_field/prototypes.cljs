
(ns pretty-inputs.text-field.prototypes
    (:require [fruits.loop.api              :refer [<-walk]]
              [fruits.vector.api            :as vector]
              [pretty-build-kit.api         :as pretty-build-kit]
              [pretty-inputs.input.utils    :as input.utils]
              [pretty-inputs.text-field.adornments :as text-field.adornments]
              [metamorphic-content.api :as metamorphic-content]
              [activity-listener.api :as activity-listener]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click-f (function)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :icon-family (keyword)
  ;  :text-color (keyword or string)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [adornment-id {:keys [icon label on-click-f timeout tooltip-content] :as adornment-props}]
  (merge {:text-color :default}
         (if icon       {:icon-family    :material-symbols-outlined
                         :icon-size      :s})
         (if label      {:font-size      :xxs
                         :letter-spacing :auto
                         :line-height    :text-block})
         (if on-click-f      {:click-effect   :opacity})
         (if tooltip-content {:tooltip-position :left})
         (-> adornment-props)
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if timeout {:on-click-f #(activity-listener/start-countdown! adornment-id {:step 1000 :timeout timeout :on-start-f on-click-f})})))

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
  (if emptiable? (let [empty-field-adornment (text-field.adornments/empty-field-adornment field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment))
                 (-> end-adornments)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (<-walk {:font-size       :s
           :focus-id        field-id
           :form-id         field-id
           :font-weight     :normal
           :line-height     :text-block
           :type            :text
           :value-path      (input.utils/default-value-path field-id)}
          (fn [%] (merge % (if border-color {:border-position :all
                                             :border-width    :xxs})
                           (if marker-color {:marker-position :tr})))
          (fn [%] (merge % field-props))
          (fn [%] (merge % {:on-blur       [:pretty-inputs.text-field/field-blurred field-id %]
                            :on-focus      [:pretty-inputs.text-field/field-focused field-id %]
                            :on-type-ended [:pretty-inputs.text-field/type-ended    field-id %]}))))
