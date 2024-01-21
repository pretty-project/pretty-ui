
(ns components.list-item-avatar.views
    (:require [components.user-avatar.views :as user-avatar.views]
              [fruits.random.api            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-avatar
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  [avatar-id avatar-props]
  [:div {:class :c-list-item-avatar}
        [user-avatar.views/component avatar-id avatar-props]])

(defn component
  ; @note
  ; For more information, check out the documentation of the ['user-avatar'](/pretty-ui/cljs/pretty-elements/api.html#user-avatar) element.
  ;
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
  ;
  ; @usage
  ; [list-item-avatar {...}]
  ;
  ; @usage
  ; [list-item-avatar :my-list-item-avatar {...}]
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   ; @note (tutorials#parametering)
   (fn [_ avatar-props]
       (let [] ; avatar-props (list-item-avatar.prototypes/avatar-props-prototype avatar-props)
            [list-item-avatar avatar-id avatar-props]))))
