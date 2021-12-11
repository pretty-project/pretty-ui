
(ns app-extensions.website-components.api
    (:require [app-extensions.website-components.engine]
              [app-extensions.website-components.footer :as footer]
              [app-extensions.website-components.hero   :as hero]
              [app-extensions.website-components.menu   :as menu]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-extensions.website-components.footer
(def scroll-to-top footer/scroll-to-top)

; app-extensions.website-components.hero
(def scroll-down hero/scroll-down)

; app-extensions.website-components.menu
(def menu menu/view)
