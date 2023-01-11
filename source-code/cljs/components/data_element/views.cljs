
(ns components.data-element.views
    (:require [components.data-element.prototypes :as data-element.prototypes]
              [elements.api                       :as elements]
              [random.api                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-element-label
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:disabled? (boolean)(opt)
  ;  :font-size (keyword)
  ;  :helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :marked? (boolean)(opt)}
  [_ {:keys [disabled? font-size helper info-text label marked?]}]
  ; XXX#0510 (source-code/app/components/frontend/data_element/prototypes.cljs)
  (if label [elements/label {:content             label
                             :disabled?           disabled?
                             :font-size           font-size
                             :helper              helper
                             :horizontal-position :left
                             :info-text           info-text
                             :marked?             marked?
                             :selectable?         false}]))

(defn- data-element-value
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:copyable? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword)
  ;  :placeholder (metamorphic-content)(opt)}
  ; @param (metamorphic-content) value
  [_ {:keys [copyable? disabled? font-size placeholder]} value]
  [elements/text {:copyable?           copyable?
                  :color               :muted
                  :content             value
                  :disabled?           disabled?
                  :font-size           font-size
                  :horizontal-position :left
                  :line-height         :text-block
                  :placeholder         placeholder
                  :selectable?         true}])

(defn- data-element-values
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:value (metamorphic-contents in vector)(opt)}
  [element-id {:keys [value] :as element-props}]
  ; XXX#0516
  (letfn [(f [values value] (conj values [data-element-value element-id element-props value]))]
         (reduce f [:<>] value)))

(defn- data-element
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:indent (map)(opt)}
  [element-id {:keys [indent] :as element-props}]
  [elements/blank {:indent  indent
                   :content [:<> [data-element-label  element-id element-props]
                                 [data-element-values element-id element-props]]}])

(defn component
  ; @param (keyword)(opt) element-id
  ; @param (map) element-props
  ; {:copyable? (boolean)(opt)
  ;   Default: false
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;   Default: :s
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :marked? (boolean)(opt)
  ;   Default: false
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :value (metamorphic-content or metamorphic-contents in vector)(opt)}
  ;
  ; @usage
  ; [data-element {...}]
  ;
  ; @usage
  ; [data-element :my-data-element {...}]
  ([element-props]
   [component (random/generate-keyword) element-props])

  ([element-id element-props]
   ; XXX#0516
   ; A data-element komponens value tulajdonságának típusa lehet metamorphic-content
   ; típus vagy metamorphic-content típusok vektorban (egyszerre több értéket is
   ; fel tud sorolni).
   (let [element-props (data-element.prototypes/element-props-prototype element-props)]
        [data-element element-id element-props])))
