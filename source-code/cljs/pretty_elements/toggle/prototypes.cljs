
(ns pretty-elements.toggle.prototypes
    (:require [dom.api :as dom]
              [react-references.api :as react-references]
              [pretty-rules.api :as pretty-rules]
              [pretty-standards.api :as pretty-standards]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-props-prototype
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)
  ;  :href (string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :on-click-f (function)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :click-effect (keyword)
  ;  :focus-id (keyword)
  ;  :marker-position (keyword)}
  [toggle-id {:keys [border-color href marker-color on-click-f] :as toggle-props}]
  (let [set-reference-f (react-references/set-reference-f toggle-id)])
  (merge {:focus-id toggle-id}
         (if marker-color {:marker-position :tr})
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (if href         {:click-effect :opacity})
         (if on-click-f   {:click-effect :opacity})
         (-> toggle-props)
         (if href       {:on-mouse-up dom/blur-active-element!})
         (if on-click-f {:on-mouse-up dom/blur-active-element!})))
         ; :text-selectable? false
         ;(pretty-properties/default-size-props {:size-unit :full-block})
         ;(pretty-properties/default-wrapper-size-props {})))
         ;(pretty-standards/standard-wrapper-size-props)))
         ;(pretty-rules/auto-disable-highlight-color)))
         ;(pretty-rules/auto-disable-hover-color)))
         ;(pretty-rules/apply-auto-border-crop)))
         ;(pretty-rules/auto-disable-effects)))
         ;(pretty-rules/auto-disable-cursor)))
         ;(pretty-rules/auto-disable-mouse-events)))
         ;(pretty-rules/auto-set-click-effect)))
         ; (pretty-rules/auto-blur-click-events)))
         ;(pretty-rules/auto-adapt-wrapper-size)))
