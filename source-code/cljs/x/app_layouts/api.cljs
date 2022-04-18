
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.api
    (:require [x.app-layouts.form-a.helpers :as form-a.helpers]
              [x.app-layouts.form-a.views   :as form-a.views]
              [x.app-layouts.layout-a.views :as layout-a.views]
              [x.app-layouts.layout-b.views :as layout-b.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-layouts.form-a.helpers
(def input-block-attributes  form-a.helpers/input-block-attributes)
(def input-row-attributes    form-a.helpers/input-row-attributes)
(def input-column-attributes form-a.helpers/input-column-attributes)

; x.app-layouts.form-a.views
(def input-group-label  form-a.views/input-group-label)
(def input-group-header form-a.views/input-group-header)

; x.app-layouts.layout-a.views
(def layout-a layout-a.views/layout)

; x.app-layouts.layout-b.views
(def layout-b layout-b.views/layout)
