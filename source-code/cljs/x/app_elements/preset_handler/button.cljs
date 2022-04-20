
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.preset-handler.button)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BUTTON-PROPS-PRESETS
     {; Defaults:
      :default      {:horizontal-align :left}
      :muted        {:color            :muted
                     :horizontal-align :left}
      :primary      {:color            :primary
                     :horizontal-align :left}
      :secondary    {:color            :secondary
                     :horizontal-align :left}
      :warning      {:color            :warning
                     :horizontal-align :left}
      ; *****
      :accept       {:color            :primary
                     :horizontal-align :left
                     :label            :accept!}
      :action       {:color            :primary
                     :horizontal-align :left}
      :back         {:horizontal-align :left
                     :icon             :arrow_back
                     :label            :back!}
      :cancel       {:horizontal-align :left
                     :label            :cancel!}
      :close        {:horizontal-align :left
                     :label            :close!}
      :delete       {:color            :warning
                     :horizontal-align :left
                     :label            :delete!}
      :duplicate    {:horizontal-align :left
                     :label            :duplicate!}
      :help         {:horizontal-align :left
                     :icon             :help_outline
                     :label            :help}
      :language     {:horizontal-align :left
                     :icon             :translate
                     :label            :language}
      :logout       {:color            :warning
                     :horizontal-align :left
                     :icon             :logout
                     :label            :logout!}
      :more-options {:horizontal-align :left
                     :icon             :list
                     :label            :more-options}
      :order-by     {:horizontal-align :left
                     :label            :order-by}
      :reorder      {:horizontal-align :left
                     :label            :reorder}
      :restore      {:horizontal-align :left
                     :label            :restore!}
      :save         {:color            :primary
                     :horizontal-align :left
                     :label            :save!}
      :select       {:color            :primary
                     :horizontal-align :left
                     :label            :select}
      :settings     {:horizontal-align :left
                     :icon             :settings
                     :label            :settings}
      :upload       {:color            :primary
                     :horizontal-align :left
                     :label            :upload!}})
