
(ns pretty-elements.adornment.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]
              [dom.api :as dom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ; {:href-uri (string)(opt)
  ;  :icon (keyword)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-click-f (function)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:click-effect (keyword)
  ;  :focus-id (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :on-mouse-up-f (function)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [adornment-id {:keys [href-uri icon label on-click-f tooltip-content] :as adornment-props}]
  (merge {:focus-id adornment-id}
         (if icon            {:icon-family      :material-symbols-outlined})
         (if on-click-f      {:click-effect     :opacity})
         (if tooltip-content {:tooltip-position :left})
         (-> adornment-props)
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})
         (if icon       {:icon-size :s})
         (if label      {:font-size :xxs :letter-spacing :auto :line-height :text-block})
         (if href-uri   {:on-mouse-up-f dom/blur-active-element!})
         (if on-click-f {:on-mouse-up-f dom/blur-active-element!})))
