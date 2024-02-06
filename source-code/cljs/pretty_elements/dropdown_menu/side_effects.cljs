
(ns pretty-elements.dropdown-menu.side-effects
    (:require [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-elements.surface.side-effects :as surface.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-mouse-leave-f
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id _]
  ; @bug (#7016)
  ; - Using the 'on-mouse-out' event would cause that the child elements of the dropdown menu
  ;   (such as the menu items of the implemented 'menu-bar' element) would trigger their parent's
  ;   'on-mouse-out' event (by event bubbling).
  ; - Moving the cursor over the 'menu-bar' element would cause flickering by firing the 'on-mouse-out'
  ;   events of the inner child elements.
  ; - Using the 'stopPropagation' function is not a solution, because in case the cursor
  ;   leaves the 'dropdown-menu' element directly from the 'menu-bar' element (if no padding around
  ;   the 'menu-bar' element) means that the 'on-mouse-out' event of the 'dropdown-menu' element wouldn't fire at all.
  ; - https://www.geeksforgeeks.org/how-to-disable-mouseout-events-triggered-by-child-elements
  ;   According to this article, using the 'on-mouse-leave' event instead of using the 'on-mouse-out' event solves the problem.
  (let [surface-id (pretty-elements.engine/element-id->subitem-id menu-id :surface)]
       (surface.side-effects/unmount-surface! surface-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-dropdown-content!
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (metamorphic-content) dropdown-content
  [menu-id dropdown-content]
  (let [surface-id (pretty-elements.engine/element-id->subitem-id menu-id :surface)]
       (surface.side-effects/set-surface-content! surface-id dropdown-content)))
