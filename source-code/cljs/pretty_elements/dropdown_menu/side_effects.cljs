
(ns pretty-elements.dropdown-menu.side-effects
    (:require [pretty-subitems.api :as pretty-subitems]
              [pretty-elements.expandable.side-effects :as expandable.side-effects]
              [dynamic-props.api :as dynamic-props]))

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
  ;   (such as the menu items of the implemented 'menu-bar' element) would trigger their parent's 'on-mouse-out' event (by event bubbling).
  ; - Moving the cursor over the 'menu-bar' element would cause flickering by firing the 'on-mouse-out' events of the inner child elements.
  ; - Using the 'stopPropagation' function is not a solution, because in case the cursor
  ;   leaves the 'dropdown-menu' element directly from the 'menu-bar' element (if no padding around the 'menu-bar' element) means that
  ;   the 'on-mouse-out' event of the 'dropdown-menu' element wouldn't fire at all.
  ; - https://www.geeksforgeeks.org/how-to-disable-mouseout-events-triggered-by-child-elements
  ;   According to this article, using the 'on-mouse-leave' event instead of using the 'on-mouse-out' event solves the problem.
  (expandable.side-effects/collapse-content! menu-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-dropdown-content!
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (multitype-content) dropdown-content
  [menu-id dropdown-content]
  (dynamic-props/update-props!             menu-id assoc :content dropdown-content)
  (expandable.side-effects/expand-content! menu-id))
