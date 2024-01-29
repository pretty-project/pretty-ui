
(ns pretty-website.language-selector.views
    (:require [fruits.hiccup.api                           :as hiccup]
              [fruits.random.api                           :as random]
              [pretty-elements.engine.api                           :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [pretty-website.language-selector.attributes :as language-selector.attributes]
              [pretty-website.language-selector.prototypes :as language-selector.prototypes]
              [reagent.api                                 :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- language-selector
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:languages (keywords in vector)}
  [selector-id {:keys [languages] :as selector-props}]
  [:div (language-selector.attributes/selector-attributes selector-id selector-props)
        [:div (language-selector.attributes/selector-body-attributes selector-id selector-props)
              (letfn [(f0 [language]
                          [:button (language-selector.attributes/language-button-attributes selector-id selector-props language)
                                   (name language)])]
                     (hiccup/put-with [:<>] languages f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-lifecycles
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    selector-id selector-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount selector-id selector-props))
                       :reagent-render         (fn [_ selector-props] [language-selector selector-id selector-props])}))

(defn component
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :gap (keyword, px or string)(opt)
  ;   Default: :xxs
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :languages (keywords in vector)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [language-selector {...}]
  ;
  ; @usage
  ; [language-selector :my-language-selector {...}]
  ;
  ; @usage
  ; [language-selector {:languages [:en :hu]}]
  ([selector-props]
   [component (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   ; @note (tutorials#parametering)
   (fn [_ selector-props]
       (let [selector-props (pretty-presets.engine/apply-preset                    selector-id selector-props)
             selector-props (language-selector.prototypes/selector-props-prototype selector-id selector-props)]
            [component-lifecycles selector-id selector-props]))))
