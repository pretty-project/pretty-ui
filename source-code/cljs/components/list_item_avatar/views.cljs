
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
  ; @info
  ; XXX#0720 (source-code/cljs/pretty_components/user_avatar/views.cljs)
  ; The 'list-item-avatar' component is based on the 'user-avatar' component.
  ; For more information, check out the documentation of the 'user-avatar' component.
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
   (fn [_ avatar-props] ; XXX#0106 (tutorials.api#parametering)
       (let [] ; avatar-props (list-item-avatar.prototypes/avatar-props-prototype avatar-props)
            [list-item-avatar avatar-id avatar-props]))))
