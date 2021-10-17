
(ns extensions.website-components.api
    (:require [extensions.website-components.engine]
              [extensions.website-components.footer :as footer]
              [extensions.website-components.hero   :as hero]
              [extensions.website-components.menu   :as menu]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; extensions.website-components.footer
(def scroll-to-top footer/scroll-to-top)

; extensions.website-components.hero
(def scroll-down hero/scroll-down)

; extensions.website-components.menu
(def menu menu/view)
