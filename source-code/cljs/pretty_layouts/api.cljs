
(ns pretty-layouts.api
    (:require [pretty-layouts.body.views    :as body.views]
              [pretty-layouts.footer.views  :as footer.views]
              [pretty-layouts.header.views  :as header.views]
              [pretty-layouts.popup.views   :as popup.views]
              [pretty-layouts.sidebar.views :as sidebar.views]
              [pretty-layouts.surface.views :as surface.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def body    body.views/view)
(def footer  footer.views/view)
(def header  header.views/view)
(def popup   popup.views/view)
(def sidebar sidebar.views/view)
(def surface surface.views/view)
