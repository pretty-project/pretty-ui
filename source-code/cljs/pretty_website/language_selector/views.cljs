
(ns pretty-website.language-selector.views
    (:require [fruits.hiccup.api                           :as hiccup]
              [fruits.random.api                           :as random]
              [pretty-presets.api                          :as pretty-presets]
              [pretty-website.language-selector.attributes :as language-selector.attributes]
              [pretty-website.language-selector.prototypes :as language-selector.prototypes]))

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

(defn component
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :gap (keyword, px or string)(opt)
  ;   Default: :xxs
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :languages (keywords in vector)
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
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
       (let [selector-props (pretty-presets/apply-preset                           selector-props)
             selector-props (language-selector.prototypes/selector-props-prototype selector-props)]
            [language-selector selector-id selector-props]))))
