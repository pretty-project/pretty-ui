
(ns pretty-elements.content-swapper.side-effects
    (:require [transition-controller.api :as transition-controller]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swap-content!
  ; @description
  ; Changes the content of a specific 'content-swapper' element.
  ;
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; (swap-content! :my-content-swapper [:div "My content"])
  ;
  ; @usage
  ; (swap-content! :my-content-swapper [:div "My content"] {:animation-direction :rtl})
  ([swapper-id content]
   (swap-content! swapper-id content {}))

  ([swapper-id content options]
   (let [content [:div {:class :pe-content-swapper--content} (metamorphic-content/compose content)]]
        (pretty-elements.engine/update-element-dynamic-props! swapper-id options)
        (transition-controller/set-content!                   swapper-id content))))
