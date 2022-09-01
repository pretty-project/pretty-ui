
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.search-field
    (:require [mid-fruits.candy                     :as candy :refer [param]]
              [x.app-components.api                 :as components]
              [x.app-core.api                       :as a :refer [r]]
              [x.app-elements.engine.api            :as engine]
              [x.app-elements.text-field.prototypes :as text-field.prototypes]
              [x.app-elements.text-field.views      :as text-field.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.text-field.views



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-enter (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:start-adornments (maps in vector)}
  [_ {:keys [on-enter] :as field-props}]
  (merge {}
         (if on-enter ; XXX#6054
                      {:start-adornments [{:icon :search :on-click on-enter :tab-indexed? false}]}
                      {:start-adornments [{:icon :search}]})
         (param field-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :auto-focus? (boolean)(constant)(opt)
  ;    Default: false
  ;   :border-color (keyword or string)(opt)
  ;    :default, :primary, :secondary,
  ;    Default: :default
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :info-text (metamorphic-content)(opt)
  ;   :initial-value (string)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :modifier (function)(opt)
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :on-empty (metamorphic-event)(constant)(opt)
  ;   :on-enter (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :style (map)(opt)
  ;   :surface (metamorphic-content)(opt)
  ;   :value-path (vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/search-field {...}]
  ;
  ; @usage
  ;  [elements/search-field :my-search-field {...}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (as-> field-props % (field-props-prototype                               field-id %)
                                         (text-field.prototypes/field-props-prototype field-id %))]
        [engine/stated-element field-id
                               {:render-f      #'text-field.views/element
                                :element-props field-props
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-text-field-props field-id]

                                ; TEMP
                                :destructor (if (:autoclear? field-props) [:db/remove-item! (:value-path field-props)])}])))
                                ; TEMP
