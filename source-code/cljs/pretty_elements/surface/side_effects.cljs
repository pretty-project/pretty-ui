
(ns pretty-elements.surface.side-effects
    (:require [dynamic-props.api           :as dynamic-props]
              [metamorphic-content.api     :as metamorphic-content]
              [pretty-elements.surface.env :as surface.env]
              [transition-controller.api   :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mount-surface!
  ; @description
  ; Mounts the the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (mount-surface! :my-surface)
  [surface-id]
  (dynamic-props/update-props! surface-id assoc :mounted? true))

(defn unmount-surface!
  ; @description
  ; Unmounts the 'surface' element.
  ;
  ; @param (keyword) surface-id
  ;
  ; @usage
  ; (unmount-surface! :my-surface)
  [surface-id]
  (dynamic-props/update-props! surface-id assoc :mounted? false))

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
   ; @note (pretty-elements.surface.env#0018)
   ;
   ; @note (#0015)
   ; It's important to ensure that the ':animation-direction' property is updated
   ; in the dynamic props state (every time when this function is called) even if its value is NIL!
   ; Otherwise, a previously stored value could remain in use!
   ;
   ; @note (#0016)
   ; The given content must be stored as a dynamic property ...
   ; ... to make the 'surface' element remember its dynamically updated content when it gets remounted by its side effect functions.
   ; ... to be provided as initial content for the transition controller in case the controller gets mounted by this function
   ;     and it will display its initial content after it is mounted.
   ;
   ; @note (#0017)
   ; If the transition controller is not mounted, it gets mounted by this function and it mounts with
   ; the given content as its initial content (the given content is stored as a dynamic property).
   (let [content [:div {:class :pe-surface--content} (metamorphic-content/compose content)]
         dynamic-props {:animation-direction animation-direction :content content}]
        (dynamic-props/merge-props! surface-id dynamic-props)
        (if (surface.env/surface-mounted?       surface-id)
            (transition-controller/set-content! surface-id content)
            (mount-surface!                     surface-id)))))

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
   ; @note (pretty-elements.surface.env#0018)
   ; @note (#0015)
   (let [dynamic-props {:animation-direction animation-direction}]
        (dynamic-props/merge-props! surface-id dynamic-props)
        (if (surface.env/surface-mounted?        surface-id)
            (transition-controller/show-content! surface-id)
            (mount-surface!                      surface-id)))))


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
   ; @note (pretty-elements.surface.env#0018)
   ; @note (#0015)
   (let [dynamic-props {:animation-direction animation-direction}]
        (dynamic-props/merge-props! surface-id dynamic-props)
        (if (surface.env/surface-mounted?        surface-id)
            (transition-controller/hide-content! surface-id)))))
