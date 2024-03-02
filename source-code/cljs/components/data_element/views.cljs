
(ns components.data-element.views
    (:require [components.data-element.prototypes :as data-element.prototypes]
              [fruits.random.api                  :as random]
              [pretty-elements.api                :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-element-label
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)
  ;  :helper (multitype-content)(opt)
  ;  :info-text (multitype-content)(opt)
  ;  :label (multitype-content)(opt)
  ;  :marked? (boolean)(opt)}
  [_ {:keys [disabled? font-size helper info-text label marked?]}])
  ; XXX#0510 (source-code/app/pretty_components/frontend/data_element/prototypes.cljs)
  ;(if label [pretty-elements/label {:content          label
  ;                                  :disabled?        disabled?
  ;                                  :font-size        font-size
  ;;                                  :helper           helper
    ;                                :horizontal-align :left
    ;                                :info-text        info-text
    ;                                :marked?          marked?]}])

(defn- data-element-value
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:copyable? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)
  ;  :value-placeholder (multitype-content)(opt)}
  ; @param (multitype-content) value
  [_ {:keys [copyable? disabled? font-size value-placeholder]} value]
  [pretty-elements/text {:copyable?           copyable?
                         :color               :muted
                         :content             value
                         :content-placeholder value-placeholder
                         :disabled?           disabled?
                         :font-size           font-size
                         :line-height         :text-block
                         :text-selectable?    true}])

(defn- data-element-values
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:value (multitype-contents in vector)(opt)}
  [element-id {:keys [value] :as element-props}]
  ; XXX#0516
  (letfn [(f0 [values value] (conj values [data-element-value element-id element-props value]))]
         (reduce f0 [:<>] value)))

(defn- data-element
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:indent (map)(opt)}
  [element-id {:keys [indent] :as element-props}]
  [pretty-elements/blank {:indent  indent
                          :content [:<> [data-element-label  element-id element-props]
                                        [data-element-values element-id element-props]]}])

(defn view
  ; @param (keyword)(opt) element-id
  ; @param (map) element-props
  ; {:copyable? (boolean)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :helper (multitype-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (multitype-content)(opt)
  ;  :marked? (boolean)(opt)
  ;   Default: false
  ;  :label (multitype-content)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :value (multitype-content or multitype-contents in vector)(opt)
  ;  :value-placeholder (multitype-content)(opt)}
  ;
  ; @usage
  ; [data-element {...}]
  ;
  ; @usage
  ; [data-element :my-data-element {...}]
  ([element-props]
   [view (random/generate-keyword) element-props])

  ([element-id element-props]
   ; XXX#0516
   ; A data-element komponens value tulajdonságának típusa lehet multitype-content
   ; típus vagy multitype-content típusok vektorban (egyszerre több értéket is
   ; fel tud sorolni).
   ;
   ; @note (tutorials#parameterizing)
   (fn [_ element-props]
       (let [element-props (data-element.prototypes/element-props-prototype element-props)]
            [data-element element-id element-props]))))
