
(ns pretty-renderers.content-renderer.side-effects
    (:require [dynamic-props.api :as dynamic-props]
              [multitype-content.api :as multitype-content]
              [pretty-renderers.content-renderer.env :as content-renderer.env]
              [transition-controller.api :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mount-renderer!
  ; @description
  ; Mounts the 'content-renderer' component.
  ;
  ; @param (keyword) id
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (mount-renderer! :my-content-renderer)
  [id]
  (dynamic-props/update-props! id assoc :mounted? true))

(defn unmount-renderer!
  ; @description
  ; Unmounts the 'content-renderer' component.
  ;
  ; @param (keyword) id
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (unmount-render! :my-content-renderer)
  [id]
  (dynamic-props/update-props! id assoc :mounted? false))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-renderer-content!
  ; @description
  ; Sets the content of the 'content-renderer' component.
  ;
  ; @param (keyword) id
  ; @param (multitype-content) content
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (set-renderer-content! :my-content-renderer [:div "My content"])
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (set-renderer-content! :my-content-renderer [:div "My content"] {:animation-direction :rtl})
  ([id content]
   (set-renderer-content! id content {}))

  ([id content {:keys [animation-direction] :as options}]
   ; @note (pretty-renderers.content-renderer.env#0018)
   ;
   ; @note (#0015)
   ; It's important to ensure that the ':animation-direction' property is updated
   ; in the dynamic props state (every time when this function is called) even if its value is NIL!
   ; Otherwise, a previously stored value could remain in use!
   ;
   ; @note (#0016)
   ; The given content must be stored as a dynamic property ...
   ; ... to ensure that the 'content-renderer' component remembers its dynamically updated content when it gets remounted by its side effect functions.
   ; ... to be provided as initial content for the transition controller in case the controller gets mounted by this function
   ;     and it will display its initial content after it is mounted.
   ;
   ; @note (#0017)
   ; If the transition controller is not mounted, it gets mounted by this function and it mounts with
   ; the given content as its initial content (the given content is stored as a dynamic property).
   (let [content [:div {:class :pr-content-renderer--body} (multitype-content/compose content)]
         dynamic-props {:animation-direction animation-direction :content content}]
        (dynamic-props/merge-props! id dynamic-props)
        (if (content-renderer.env/renderer-mounted? id)
            (transition-controller/set-content! id content)
            (mount-renderer!                    id)))))

(defn swap-renderer-content!
  ; @description
  ; Changes the content of the 'content-renderer' component.
  ;
  ; @param (keyword) id
  ; @param (multitype-content) content
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (swap-renderer-content! :my-content-renderer [:div "My content"])
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (swap-renderer-content! :my-content-renderer [:div "My content"] {:animation-direction :rtl})
  ([id content]
   (swap-renderer-content! id content {}))

  ([id content options]
   (set-renderer-content! id content options)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-renderer-content!
  ; @description
  ; Turns on the visibility of the 'content-renderer' component.
  ;
  ; @param (keyword) id
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (show-renderer-content! :my-content-renderer)
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (show-renderer-content! :my-content-renderer {:animation-direction :rtl})
  ([id]
   (show-renderer-content! id {}))

  ([id {:keys [animation-direction]}]
   ; @note (pretty-renderers.content-renderer.env#0018)
   ; @note (#0015)
   (let [dynamic-props {:animation-direction animation-direction}]
        (dynamic-props/merge-props! id dynamic-props)
        (if (content-renderer.env/renderer-mounted? id)
            (transition-controller/show-content! id)
            (mount-renderer!                     id)))))

(defn hide-renderer-content!
  ; @description
  ; Turns off the visibility of the 'content-renderer' component.
  ;
  ; @param (keyword) id
  ; @param (map)(opt) options
  ; {:animation-direction (keyword)(opt)}
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (hide-renderer-content! :my-content-renderer)
  ;
  ; @usage
  ; [content-renderer :my-content-renderer {...}]
  ; (hide-renderer-content! :my-content-renderer {:animation-direction :rtl})
  ([id]
   (hide-renderer-content! id {}))

  ([id {:keys [animation-direction]}]
   ; @note (pretty-renderers.content-renderer.env#0018)
   ; @note (#0015)
   (let [dynamic-props {:animation-direction animation-direction}]
        (dynamic-props/merge-props! id dynamic-props)
        (if (content-renderer.env/renderer-mounted? id)
            (transition-controller/hide-content! id)))))
