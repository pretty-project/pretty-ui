
(ns pretty-elements.surface.side-effects
    (:require [transition-controller.api :as transition-controller]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-surface!
  ; @description
  ; Turns on the visibility of the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (show-surface! :my-surface)
  [surface-id]
  (pretty-elements.engine/show-element! surface-id {}))

(defn hide-surface!
  ; @description
  ; Turns off the visibility of the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (hide-surface! :my-surface)
  [surface-id]
  (pretty-elements.engine/hide-element! surface-id {}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-surface-content!
  ; @description
  ; Sets the content of the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; (set-surface-content! :my-surface [:div "My content"])
  ;
  ; @usage
  ; (set-surface-content! :my-surface [:div "My content"] {:animation-direction :rtl})
  ([surface-id content]
   (set-surface-content! surface-id content {}))

  ([surface-id content {:keys [animation-direction] :as options}]
   ; @note (#0015)
   ; It's important to ensure that the 'animation-direction' property is updated
   ; in the dynamic props state (every time when this function is called) even if its value is NIL!
   ; Otherwise, a previously stored value could remain in use!
   (let [content [:div {:class :pe-surface--content} (metamorphic-content/compose content)]
         updated-props {:animation-direction animation-direction}]
        (show-surface!                                        surface-id)
        (pretty-elements.engine/update-element-dynamic-props! surface-id updated-props)
        (transition-controller/set-content!                   surface-id content))))

(defn swap-surface-content!
  ; @description
  ; Changes the content of the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; (swap-surface-content! :my-surface [:div "My content"])
  ;
  ; @usage
  ; (swap-surface-content! :my-surface [:div "My content"] {:animation-direction :rtl})
  ([surface-id content]
   (swap-surface-content! surface-id content {}))

  ([surface-id content options]
   (set-surface-content! surface-id content options)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-surface-content!
  ; @description
  ; Turns on the visibility of the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; (show-surface-content! :my-surface)
  ;
  ; @usage
  ; (show-surface-content! :my-surface {:animation-direction :rtl})
  ([surface-id]
   (show-surface-content! surface-id {}))

  ([surface-id {:keys [animation-direction]}]
   ; @note (#0015)
   (let [dynamic-props {:animation-direction animation-direction}]
        (pretty-elements.engine/update-element-dynamic-props! surface-id dynamic-props)
        (transition-controller/show-content!                  surface-id))))

(defn hide-surface-content!
  ; @description
  ; Turns off the visibility of the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; (hide-surface-content! :my-surface)
  ;
  ; @usage
  ; (hide-surface-content! :my-surface {:animation-direction :rtl})
  ([surface-id]
   (hide-surface-content! surface-id {}))

  ([surface-id {:keys [animation-direction]}]
   ; @note (#0015)
   (let [dynamic-props {:animation-direction animation-direction}]
        (pretty-elements.engine/update-element-dynamic-props! surface-id dynamic-props)
        (transition-controller/hide-content!                  surface-id))))
